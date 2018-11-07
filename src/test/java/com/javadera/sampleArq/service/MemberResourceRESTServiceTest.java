/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.javadera.sampleArq.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.javadera.sampleArq.data.MemberRepository;
import com.javadera.sampleArq.model.Member;
import com.javadera.sampleArq.rest.MemberResourceRESTService;
import com.javadera.sampleArq.util.Resources;

@RunWith(Arquillian.class)
public class MemberResourceRESTServiceTest {
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(Member.class
                    , MemberRegistration.class
                    , Resources.class
                    , MemberResourceRESTService.class
                    , MemberRepository.class)
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                // Deploy h2 test datasource
                .addAsWebInfResource("test-h2-ds.xml");
                // Deploy PostgreSQL test datasource
//              .addAsWebInfResource("test-ds.xml");
    }

    @Inject
    MemberResourceRESTService memberResourceRESTService;
    
    @Inject
    MemberRegistration memberRegistration;

    @Inject
    Logger log;

    @Before
    public void before() throws Exception {
      LocalDateTime localDateTime = LocalDateTime.now();
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuuMMddHHmmssSSS"); 
      Member newMember = new Member();
      newMember.setName("Jane Doe");
      newMember.setEmail("jane" + dtf.format(localDateTime) + "@mailinator.com");
      newMember.setPhoneNumber("2125551234");
      memberRegistration.register(newMember);
      assertNotNull(newMember.getId());
      log.info(newMember.getName() + " was persisted with id " + newMember.getId());
    }

    @After
    public void after() throws Exception {
//      Member mem = memberRegistration.selectByName("Jane Doe");
//      memberRegistration.delete(mem);
    }

    @Test
    public void testGet() throws Exception {
      List<Member> list = memberResourceRESTService.listAllMembers();
      assertNotNull(list);
    }
    
//    @Test
//    @RunAsClient
//    public void testClient() throws Exception {
//      Client client = ClientBuilder.newClient();
//      WebTarget target = client.target("http://localhost:8080/test/members/1");
//      Response response = target.request(MediaType.APPLICATION_JSON).buildGet().invoke();
//      String responseString = response.readEntity(String.class);
//      assertNotNull(responseString);
//    }
    
    @Test
    public void testLookupMember() throws Exception {
      Member member = memberResourceRESTService.lookupMemberById(1);
      assertThat(member.getId(), is(1L));
    }
}
