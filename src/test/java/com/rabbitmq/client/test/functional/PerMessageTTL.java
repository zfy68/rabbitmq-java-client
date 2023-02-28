// Copyright (c) 2007-2023 VMware, Inc. or its affiliates.  All rights reserved.
//
// This software, the RabbitMQ Java client library, is triple-licensed under the
// Mozilla Public License 2.0 ("MPL"), the GNU General Public License version 2
// ("GPL") and the Apache License version 2 ("ASL"). For the MPL, please see
// LICENSE-MPL-RabbitMQ. For the GPL, please see LICENSE-GPL2.  For the ASL,
// please see LICENSE-APACHE2.
//
// This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND,
// either express or implied. See the LICENSE file for specific language governing
// rights and limitations of this software.
//
// If you have any questions regarding licensing, please contact us at
// info@rabbitmq.com.


package com.rabbitmq.client.test.functional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;

public class PerMessageTTL extends TTLHandling {

    protected Object sessionTTL;

    @Override
    protected void publish(String msg) throws IOException {
        basicPublishVolatile(msg.getBytes(), TTL_EXCHANGE, TTL_QUEUE_NAME,
                MessageProperties.TEXT_PLAIN
                        .builder()
                        .expiration(String.valueOf(sessionTTL))
                        .build());
    }

    @Override
    protected AMQP.Queue.DeclareOk declareQueue(String name, Object ttlValue) throws IOException {
        this.sessionTTL = ttlValue;
        return this.channel.queueDeclare(name, false, true, false, null);
    }

    @Test public void expiryWhenConsumerIsLateToTheParty() throws Exception {
        declareAndBindQueue(500);

        publish(MSG[0]);
        this.sessionTTL = 100;
        publish(MSG[1]);

        Thread.sleep(200);

        QueueingConsumer c = new QueueingConsumer(channel);
        channel.basicConsume(TTL_QUEUE_NAME, c);

        assertNotNull(c.nextDelivery(100), "Message unexpectedly expired");
        assertNull(c.nextDelivery(100), "Message should have been expired!!");
    }

    @Test public void restartingExpiry() throws Exception {
        final String expiryDelay = "2000";
        declareDurableQueue(TTL_QUEUE_NAME);
        bindQueue();
        channel.basicPublish(TTL_EXCHANGE, TTL_QUEUE_NAME,
                MessageProperties.MINIMAL_PERSISTENT_BASIC
                        .builder()
                        .expiration(expiryDelay)
                        .build(), new byte[]{});
        restart();
        Thread.sleep(Integer.parseInt(expiryDelay));
        try {
            assertNull(get(), "Message should have expired after broker restart");
        } finally {
            deleteQueue(TTL_QUEUE_NAME);
        }
    }

}
