/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package com.example.agent;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.drools.core.time.SessionPseudoClock;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleTest {
    static final Logger LOG = LoggerFactory.getLogger(RuleTest.class);

    @Test
    public void test() {
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kContainer = kieServices.getKieClasspathContainer();
        Results verifyResults = kContainer.verify();
        for (Message m : verifyResults.getMessages()) {
            LOG.info("{}", m);
        }

        LOG.info("Creating kieBase");
        KieBase kieBase = kContainer.getKieBase();

        LOG.info("There should be rules: ");
        for ( KiePackage kp : kieBase.getKiePackages() ) {
            for (Rule rule : kp.getRules()) {
                LOG.info("kp " + kp + " rule " + rule.getName());
            }
        }

        LOG.info("Creating kieSession");
        KieSession session = kieBase.newKieSession();

        try {
            LOG.info("Populating globals");
            //Set<Agent> check = new HashSet<Agent>();
            Set<Integer> checkMax = new HashSet<Integer>();
            //session.setGlobal("controlSet", check);
            session.setGlobal("controlSet", checkMax);

            LOG.info("Now running data");

            Agent agent1 = new Agent("Kayton", 60, -1);
            session.insert(agent1);
            //session.fireAllRules();

            Agent agent2 = new Agent("Happy", 80, -1);
            session.insert(agent2);
            //session.fireAllRules();

            Agent agent3 = new Agent("Yuki", 70, -1);
            session.insert(agent3);

            Agent agent4 = new Agent("Calvin", 65, -1);
            session.insert(agent4);

            session.fireAllRules();

            LOG.info("Final checks");

            //assertEquals("Size of object in Working Memory is 3", 3, session.getObjects().size());
            System.out.println("Agent list size is " + checkMax.size());
            /*
            Iterator<Agent> iterator = checkMax.iterator();
            Agent top= new Agent("",0,0);
            

            while (iterator.hasNext()){
                top= iterator.next();
                System.out.println("Agent Rank " + top.getRank() + " is " + top.getName());
            }
             */
            Iterator<Integer> iterator = checkMax.iterator();
            //Agent top= new Agent("",0,0);
            

            while (iterator.hasNext()){
                int i = ((Integer) iterator.next()).intValue();
                System.out.println("Agent Rank " + " has " + i);
            }

            

        } finally {
            session.dispose();
        }
    }
}