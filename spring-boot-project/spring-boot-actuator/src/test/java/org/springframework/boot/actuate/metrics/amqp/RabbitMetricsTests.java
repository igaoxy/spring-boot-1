/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.actuate.metrics.amqp;

import com.rabbitmq.client.ConnectionFactory;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link RabbitMetrics}.
 *
 * @author Stephane Nicoll
 */
public class RabbitMetricsTests {

	@Test
	public void connectionFactoryIsInstrumented() {
		ConnectionFactory connectionFactory = mockConnectionFactory();
		SimpleMeterRegistry registry = new SimpleMeterRegistry();
		new RabbitMetrics(connectionFactory, "rabbit", null).bindTo(registry);
		assertThat(registry.find("rabbit.connections").meter()).isPresent();
	}

	@Test
	public void connectionFactoryWithTagsIsInstrumented() {
		ConnectionFactory connectionFactory = mockConnectionFactory();
		SimpleMeterRegistry registry = new SimpleMeterRegistry();
		new RabbitMetrics(connectionFactory, "test", Tags.zip("env", "prod"))
				.bindTo(registry);
		assertThat(registry.find("test.connections").tags("env", "prod").meter())
				.isPresent();
		assertThat(registry.find("test.connections").tags("env", "dev").meter())
				.isNotPresent();
	}

	private ConnectionFactory mockConnectionFactory() {
		return mock(ConnectionFactory.class);
	}

}
