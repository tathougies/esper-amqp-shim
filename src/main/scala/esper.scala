package esperShim

//import com.espertech.esperio.http.EsperIOHTTPAdapterPlugin
import com.espertech.esper.client.Configuration
import com.espertech.esper.client.EPServiceProviderManager
import com.espertech.esper.client.EPServiceProvider
import com.espertech.esper.client.UpdateListener
//import EsperUtil._
import com.espertech.esper.client.EventBean

object EsperShim {

  def main ( args: Array[String] ) {
    val config = new Configuration
    config.configure

    val epService = EPServiceProviderManager.getProvider(this.getClass.getName, config)
    epService.initialize

    val epAdministrator = epService.getEPAdministrator

    Iterator.continually(Console.readLine)
      .takeWhile(_ != null)
      .withFilter(_.nonEmpty)
      .foreach(line => epAdministrator.createEPL(line));

    startAllDataflows(epService)

    while (true) { };
  }

  def startAllDataflows ( epService: EPServiceProvider ) {
    var dataflows = epService.getEPRuntime().getDataFlowRuntime().getDataFlows
    dataflows.foreach(dfName =>
      { val dfInstance = epService.getEPRuntime().getDataFlowRuntime().instantiate(dfName);
        dfInstance.start(); });
  }

}
