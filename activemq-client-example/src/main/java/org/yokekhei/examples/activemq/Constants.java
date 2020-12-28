package org.yokekhei.examples.activemq;

public class Constants {
	public enum Protocol {
		OPENWIRE("OpenWire", new Integer(1)),
		AMQP("AMQP", new Integer(2)),
		STOMP("STOMP", new Integer(3));
		
		private final String name;
		private final Integer value;
		
		Protocol(String name, Integer value) {
			this.name = name;
			this.value = value;
		}
		
		public String toString() {
			return name;
		}
		
		public int toIntValue() {
			return value.intValue();
		}
	}
	
}
