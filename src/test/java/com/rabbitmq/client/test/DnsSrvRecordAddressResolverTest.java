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

package com.rabbitmq.client.test;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.DnsSrvRecordAddressResolver;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class DnsSrvRecordAddressResolverTest {

    @Test public void recordsParsedAndSorted() throws IOException {
        DnsSrvRecordAddressResolver resolver = new DnsSrvRecordAddressResolver("rabbitmq") {
            @Override
            protected List<SrvRecord> lookupSrvRecords(String service, String dnsUrls) throws IOException {
                return Arrays.asList(
                    DnsSrvRecordAddressResolver.SrvRecord.fromSrvQueryResult("20 0 5269 alt2.xmpp-server.l.google.com."),
                    DnsSrvRecordAddressResolver.SrvRecord.fromSrvQueryResult("30 0 5269 alt3.xmpp-server.l.google.com."),
                    DnsSrvRecordAddressResolver.SrvRecord.fromSrvQueryResult("10 0 5269 alt1.xmpp-server.l.google.com."),
                    DnsSrvRecordAddressResolver.SrvRecord.fromSrvQueryResult("50 0 5269 alt5.xmpp-server.l.google.com."),
                    DnsSrvRecordAddressResolver.SrvRecord.fromSrvQueryResult("40 0 5269 alt4.xmpp-server.l.google.com.")
                );
            }
        };

        List<Address> addresses = resolver.getAddresses();
        assertThat(addresses.size()).isEqualTo(5);
        assertThat(addresses.get(0).getHost()).isEqualTo("alt1.xmpp-server.l.google.com");
        assertThat(addresses.get(1).getHost()).isEqualTo("alt2.xmpp-server.l.google.com");
        assertThat(addresses.get(2).getHost()).isEqualTo("alt3.xmpp-server.l.google.com");
        assertThat(addresses.get(3).getHost()).isEqualTo("alt4.xmpp-server.l.google.com");
        assertThat(addresses.get(4).getHost()).isEqualTo("alt5.xmpp-server.l.google.com");
    }

}
