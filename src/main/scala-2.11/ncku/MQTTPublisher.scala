package ncku
import org.apache.log4j.{Level, Logger}
import org.eclipse.paho.client.mqttv3._
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.mqtt._
import org.apache.spark.SparkConf
/**
  * Created by steve02 on 2017/6/2.
  */
  object MQTTPublisher {

    def main(args: Array[String]) {
      if (args.length < 2) {
        System.err.println("Usage: MQTTPublisher <MqttBrokerUrl> <topic>")
        System.exit(1)
      }

      // Set logging level if log4j not configured (override by adding log4j.properties to classpath)
      if (!Logger.getRootLogger.getAllAppenders.hasMoreElements) {
        Logger.getRootLogger.setLevel(Level.WARN)
      }

      val Seq(brokerUrl, topic) = args.toSeq

      var client: MqttClient = null

      try {
        val persistence = new MemoryPersistence()
        client = new MqttClient(brokerUrl, MqttClient.generateClientId(), persistence)

        client.connect()

        val msgtopic = client.getTopic(topic)
        val msgContent = "hello mqtt demo for spark streaming"
        val message = new MqttMessage(msgContent.getBytes("utf-8"))

        while (true) {
          try {
            msgtopic.publish(message)
            println(s"Published data. topic: ${msgtopic.getName()}; Message: $message")
          } catch {
            case e: MqttException if e.getReasonCode == MqttException.REASON_CODE_MAX_INFLIGHT =>
              Thread.sleep(10)
              println("Queue is full, wait for to consume data from the message queue")
          }
        }
      } catch {
        case e: MqttException => println("Exception Caught: " + e)
      } finally {
        if (client != null) {
          client.disconnect()
        }
      }
    }
  }


