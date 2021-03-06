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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

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

import com.javadera.sampleArq.model.Member;
import com.javadera.sampleArq.util.Resources;

@RunWith(Arquillian.class)
public class MemberRegistrationTest {
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(Member.class, MemberRegistration.class, Resources.class)
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                // Deploy h2 test datasource
                .addAsWebInfResource("test-h2-ds.xml");
                // Deploy PostgreSQL test datasource
//              .addAsWebInfResource("test-ds.xml");
    }

    @Inject
    MemberRegistration memberRegistration;

    @Inject
    Logger log;

    @Before
    public void before() throws Exception {
        Member newMember = new Member();
        newMember.setName("Jane Doe");
        newMember.setEmail("jane@mailinator.com");
        newMember.setPhoneNumber("2125551234");
        memberRegistration.register(newMember);
        assertNotNull(newMember.getId());
        log.info(newMember.getName() + " was persisted with id " + newMember.getId());
    }

    @After
    public void after() throws Exception {
    	Member mem = memberRegistration.selectByName("Jane Doe");
    	memberRegistration.delete(mem);
    }

//    @Test
//    public void testRegister() throws Exception {
//        Member newMember = new Member();
//        newMember.setName("Jane Doe");
//        newMember.setEmail("jane@mailinator.com");
//        newMember.setPhoneNumber("2125551234");
//        memberRegistration.register(newMember);
//        assertNotNull(newMember.getId());
//        log.info(newMember.getName() + " was persisted with id " + newMember.getId());
//    }

    @Test
    public void testSelectId() throws Exception {
    	Member m = memberRegistration.selectByName("Jane Doe");
    	Member mem = memberRegistration.select(m.getId());
    	assertEquals(m.getId(), mem.getId());
        log.info(mem.getName() + " was selected with id " + mem.getId());
    }

    @Test
    public void testSelectName() throws Exception {
    	Member mem = memberRegistration.selectByName("Jane Doe");
    	assertThat(mem.getName(), is("Jane Doe") );
        log.info(mem.getName() + " was selected with id " + mem.getId());
    }

    @Test
    public void testSelectEmail() throws Exception {
    	Member mem = memberRegistration.selectByName("Jane Doe");
    	assertThat(mem.getEmail(), is("jane@mailinator.com") );
        log.info(mem.getEmail() + " was selected with id " + mem.getId());
    }

    @Test
    public void testSelectPhoneNum() throws Exception {
    	Member mem = memberRegistration.selectByName("Jane Doe");
    	assertThat(mem.getPhoneNumber(), is("2125551234") );
        log.info(mem.getPhoneNumber() + " was selected with id " + mem.getId());
    }
}
