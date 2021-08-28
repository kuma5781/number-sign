package domain.`object`.db

import java.io.File

import com.typesafe.config.ConfigFactory

object DBProperties {
	// conf/application.confの値を参照
	private val config = ConfigFactory.parseFile(new File("conf/application.conf"))
	val driver = config.getString("db.default.driver")
	val url = config.getString("db.default.url")
	val username = config.getString("db.default.username")
	val password = config.getString("db.default.password")
}
