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


package com.rabbitmq.client.test.server;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
		Permissions.class,
    DurableBindingLifecycle.class,
    DeadLetterExchangeDurable.class,
    EffectVisibilityCrossNodeTest.class,
    ExclusiveQueueDurability.class,
    AbsentQueue.class,
    AlternateExchangeEquivalence.class,
    MemoryAlarms.class,
    MessageRecovery.class,
    Firehose.class,
    PersistenceGuarantees.class,
    Shutdown.class,
    BlockedConnection.class,
    ChannelLimitNegotiation.class,
    LoopbackUsers.class,
		XDeathHeaderGrowth.class,
		PriorityQueues.class,
		TopicPermissions.class
})
public class ServerTestSuite {

}
