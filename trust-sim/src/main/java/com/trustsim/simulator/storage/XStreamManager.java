package com.trustsim.simulator.storage;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import com.trustsim.synthesiser.AgentSystem;

import java.io.IOException;

public class XStreamManager {

  private XStream xStream = new XStream();

  public XStreamManager() {
    xStream.addPermission(NoTypePermission.NONE);
    xStream.addPermission(NullPermission.NULL);
    xStream.addPermission(PrimitiveTypePermission.PRIMITIVES);
    xStream.allowTypesByWildcard(new String[] { "com.trustsim.**" });
  }


  public String encodeToXML(AgentSystem agentSystem) throws IOException {
    String xml = xStream.toXML(agentSystem);
    System.out.println((xml));
    return xml;
  }

  public Object decodeFromXML(String xmlObject) {
    return xStream.fromXML(xmlObject);
  }

//  public void exportAgentGraph(Graph graph) throws IOException {
//    String graphName = graph.getGraphName();
//    xStream.toXML(graph, new FileWriter(graphName + ".xml", false));
//  }
//
//  public Graph importAgentGraph(String graphName) {
//    return (Graph) xStream.fromXML(graphName);
//  }
//
//  public void exportAgentSystem(AgentSystem system) throws IOException {
//    String systemName = system.getSystemName();
//    xStream.toXML(system, new FileWriter(systemName + ".xml", false));
//    //System.out.println(xml);
//  }
//
//  public AgentSystem importAgentSystem(String systemName) {
//    return (AgentSystem) xStream.fromXML(systemName);
//  }
}