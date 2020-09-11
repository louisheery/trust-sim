package com.trustsim.evaluator;

import com.trustsim.simulator.agent.AgentType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AgentTypeWrapper implements Comparable<AgentTypeWrapper> {

  private final Map<Integer, AgentType> agentTypesMap = new HashMap<>();

  /**
   * Constructor which accepts an initial AgentType object, and the simulation time from which that
   * AgentType is applicable from.
   *
   * @param agentType represents the agent type of an agent
   * @param agentTypeStartTime represents the simulation time after which an agent has that agent
   *     type
   */
  public AgentTypeWrapper(AgentType agentType, int agentTypeStartTime) {
    agentTypesMap.put(agentTypeStartTime, agentType);
  }

  /**
   * Constructor which accepts an initial AgentType object, and the simulation time from which that
   * AgentType is applicable from.
   *
   * @param agentTypes represent a series of multiple agent types of an agent
   * @param typeChangeTimes represents the simulation times after which an agent has each of the
   *     supplied agent type
   */
  public AgentTypeWrapper(List<AgentType> agentTypes, List<Integer> typeChangeTimes) {
    for (int i = 0; i < agentTypes.size() && i < typeChangeTimes.size(); i++) {
      agentTypesMap.put(typeChangeTimes.get(i), agentTypes.get(i));
    }
  }

  /**
   * Function returns map of 1 or more agent types, and the time in a simulation after which the
   * agent has that AgentType property.
   *
   * @return map of agent types and their simulation start times
   */
  public Map<Integer, AgentType> getAgentTypesMap() {
    return agentTypesMap;
  }

  /**
   * Function returns a list of 1 or more times in a simulation when an agent changes its AgentType
   * property.
   *
   * @return list of simulation start times
   */
  public List<Integer> getTypeChangeTimes() {
    return new ArrayList<>(agentTypesMap.keySet());
  }

  /**
   * Function overrides default toString function.
   *
   * @return string representation of object
   */
  @Override
  public String toString() {
    StringBuilder out = new StringBuilder();

    int i = 0;
    for (Map.Entry<Integer, AgentType> entry : agentTypesMap.entrySet()) {
      out.append(entry.getValue().toString());
      out.append(((i != agentTypesMap.size() - 1) ? " → " : ""));
      i++;
    }

    return out.toString();
  }

  /**
   * Function overrides compareTo function of object.
   *
   * @return int representing object equality (return == 0) or unequality (return != 0)
   */
  @Override
  public int compareTo(AgentTypeWrapper o) {
    if (!this.agentTypesMap.equals(o.getAgentTypesMap())) {
      return 1;
    }
    return 0;
  }

  /**
   * Function overrides hasCode function of object.
   *
   * @return int representing hashCode of object
   */
  @Override
  public int hashCode() {
    List<Integer> keys = new ArrayList<>(agentTypesMap.keySet());
    List<AgentType> values = new ArrayList<>(agentTypesMap.values());
    Collections.sort(keys);
    Collections.sort(values);
    return Objects.hash(keys, values);
  }
}
