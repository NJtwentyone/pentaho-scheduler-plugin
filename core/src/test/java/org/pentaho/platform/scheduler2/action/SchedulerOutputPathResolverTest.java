package org.pentaho.platform.scheduler2.action;

import junit.framework.TestCase;

public class SchedulerOutputPathResolverTest extends TestCase {

  public void testConcat_EE_PVFS() { // FIXME more to enterprise code base

    SchedulerOutputPathResolver testInstance = new SchedulerOutputPathResolver( );
    assertNotNull( testInstance );

    String argDirectoryPVFS = "pvfs://somBucket/someFolder/another-folder";
    String argFilename = "simple_file_name.pdf";
    String expectedFullPathPVFS = "pvfs://somBucket/someFolder/another-folder/simple_file_name.pdf";

    String actualFullPath = testInstance.concat( argDirectoryPVFS, argFilename );
    assertEquals( expectedFullPathPVFS, actualFullPath );
  }

  public void testConcat_CE_Repo_Home_Admin() {

    SchedulerOutputPathResolver testInstance = new SchedulerOutputPathResolver( );
    assertNotNull( testInstance );

    String argDirectoryRepoHomeAdmin = "/home/admin";
    String argFilename2 = "simple_file_name2.pdf";
    String expectedFullPathRepoHomeAdmdin = "/home/admin/simple_file_name2.pdf";

    String actualFullPath = testInstance.concat( argDirectoryRepoHomeAdmin, argFilename2 );
    assertEquals( expectedFullPathRepoHomeAdmdin, actualFullPath );
  }

  public void testConcat_CE_Repo_Public() {

    SchedulerOutputPathResolver testInstance = new SchedulerOutputPathResolver( );
    assertNotNull( testInstance );

    String argDirectoryRepoHome = "/public";
    String argFilename3 = "simple_file_name3.pdf";
    String expectedFullPathRepoHome = "/public/simple_file_name3.pdf";

    String actualFullPath = testInstance.concat( argDirectoryRepoHome, argFilename3 );
    assertEquals( expectedFullPathRepoHome, actualFullPath );
  }
}