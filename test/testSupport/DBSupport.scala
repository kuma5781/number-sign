package testSupport

import org.scalatest.Assertion
import repository.dao.DBAccessor

object DBSupport {
  // テスト処理の前後でテーブルを空にする
  def dbTest(tableName: String, execute: Assertion): Assertion = {
    val deleteAllSql = s"delete from $tableName"
    DBAccessor.execute(deleteAllSql)
    val result = execute
    DBAccessor.execute(deleteAllSql)
    result
  }
}
