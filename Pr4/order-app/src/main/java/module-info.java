module order.app {
    requires order.storage;
    requires order.processing;
    requires order.threads;
    requires javafaker;
    requires org.apache.commons.lang3;
    requires java.sql;
    requires static lombok;
}
