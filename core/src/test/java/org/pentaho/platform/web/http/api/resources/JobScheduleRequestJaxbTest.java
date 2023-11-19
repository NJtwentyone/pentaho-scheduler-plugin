/*!
 *
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 *
 * Copyright (c) 2023 Hitachi Vantara. All rights reserved.
 *
 */

package org.pentaho.platform.web.http.api.resources;

import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.junit.Test;
import org.pentaho.platform.api.scheduler2.IJobScheduleParam;
import org.pentaho.platform.api.scheduler2.JobParam;
import org.pentaho.platform.plugin.services.importexport.exportManifest.bindings.ObjectFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//POC
public class JobScheduleRequestJaxbTest {
  @Test
  public void testJaxbJson() throws Exception {
    // not needed, but in future -  declare in System or add property file in root of resource folder
    //    System.setProperty("javax.xml.bind.context.factory", "org.eclipse.persistence.jaxb.JAXBContextFactory");

    //Set the various properties you want
    Map<String, Object> properties = new HashMap<>();
    // NOTE could also use org.eclipse.persistence.jaxb.UnmarshallerProperties.MEDIA_TYPE, using JAXBContextProperties to future proof code
    properties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
    properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);

    // NOTE: have to setup jaxBContext with JAXBContextFactory.createContext(...) can not use JAXBContext.newInstance(...) due to no standard xml/json formats
    JAXBContext jaxbContext =
      JAXBContextFactory.createContext(new Class[]  {
        // NOTE: seems like you can use either class  org.pentaho.platform.web.http.api.resources.JobScheduleRequest or org.pentaho.platform.plugin.services.importexport.exportManifest.bindings.JobScheduleRequest
        JobScheduleRequest.class,    ObjectFactory.class}, properties);
    //    JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] {JobScheduleRequest.class}, properties);

    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

    //Overloaded methods to unmarshal from different xml sources
    String jsonFileName = "jaxb/JobScheduleRequest_update.json";
    File jsonFileJobSchedulerRequest_update = new File(getClass().getClassLoader().getResource(jsonFileName).getFile());
    StreamSource streamSource = new StreamSource(jsonFileJobSchedulerRequest_update);
    //NOTE: including class to give unmarshaller a hint, otherwise will get error related to jobName not found
    JobScheduleRequest jobScheduleRequest = jaxbUnmarshaller.unmarshal( streamSource, JobScheduleRequest.class ).getValue();

    //asserts
    assertEquals( "Top Customers (report)-manual-edit-", jobScheduleRequest.getJobName() );
    assertTrue(jobScheduleRequest.getJobParameters().size() > 8);

    // param test 1
    IJobScheduleParam param1 = jobScheduleRequest.getJobParameters().stream()
      .filter( jsp ->  "sLine".equals( jsp.getName())).findFirst().get();
    assertEquals("string", param1.getType());
    assertTrue(param1.getStringValue().contains("[Product].[All Products].[Classic Cars]"));

    // param test 2
    IJobScheduleParam param2 = jobScheduleRequest.getJobParameters().stream()
      .filter( jsp ->  "::session".equals( jsp.getName()) ).findFirst().get();
    assertEquals("string", param2.getType());
    assertTrue(param2.getStringValue().toString().contains( "09b24716-84a6-11ee-8f4b-66860150dd1e" ));

  }
  @Test
  public void testJaxbXml() throws Exception {

    Map<String, Object> properties = new HashMap<>();
    JAXBContext jaxbContext =
      JAXBContextFactory.createContext(new Class[]  {
        // NOTE: seems like you can use either class org.pentaho.platform.plugin.services.importexport.exportManifest.bindings.JobScheduleRequest or org.pentaho.platform.web.http.api.resources.JobScheduleRequest
       JobScheduleRequest.class,
//        JobScheduleParam.class,
//        org.pentaho.platform.plugin.services.importexport.exportManifest.bindings.JobScheduleParam.class,
//        org.pentaho.platform.plugin.services.importexport.exportManifest.bindings.JobScheduleRequest.class,
        ObjectFactory.class
      }, properties);
    //    JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] {JobScheduleRequest.class}, properties);

    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

    //Overloaded methods to unmarshal from different xml sources
    String xmlFileName = "jaxb/JobScheduleRequest_create.xml";
    File fileJobSchedulerRequest_update = new File(getClass().getClassLoader().getResource(xmlFileName).getFile());
    JobScheduleRequest jobScheduleRequest = (JobScheduleRequest) jaxbUnmarshaller.unmarshal( fileJobSchedulerRequest_update );

    //asserts
    assertEquals( "TestJobName1", jobScheduleRequest.getJobName() );
    assertTrue(jobScheduleRequest.getJobParameters().size() > 0);

    //test for param1
    IJobScheduleParam param1 = jobScheduleRequest.getJobParameters().stream()
      .filter( jsp -> "ParameterNameTest1".equals( jsp.getName()) ).findFirst().get();
    assertEquals("string", param1.getType());
    assertTrue(param1.getStringValue().contains( "false" ));

    // test for param2
    IJobScheduleParam param2 = jobScheduleRequest.getJobParameters().stream()
      .filter( jsp -> "ParameterNameTest2".equals( jsp.getName()) ).findFirst().get();
    assertEquals("string", param2.getType());
    assertTrue(param2.getStringValue().contains( "foo" ));

  }
}
