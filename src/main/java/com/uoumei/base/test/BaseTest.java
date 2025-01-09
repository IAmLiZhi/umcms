
package com.uoumei.base.test;

import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-*.xml"})
public abstract class BaseTest {

	/*
	 * log4j日志记录
	 */
	protected final Logger LOG = Logger.getLogger(this.getClass());

}