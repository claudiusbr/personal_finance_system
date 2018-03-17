package personalfinance
package businesslogic
package transaction

import org.joda.time.DateTime

/**
  * integration tester for DateFormatter and DateTime
  */
class DateFormatterTester extends BehaviourTester {
  val df = new DateFormatter
  "String dates passed to the `dateFromString` method of DateFormatter" should
    "be converted from yyyy(/.-)dd(/.-)dd to DateTime" in {
      df.dateFromString("2017/08/09") should be (new DateTime(2017,8,9,0,0))
      df.dateFromString("2017.08.09") should be (new DateTime(2017,8,9,0,0))
    }

  it should "be converted from dd(/.-)mm(/.-)yyyy to DateTime" in {
    df.dateFromString("09/08/2017") should be (new DateTime(2017,8,9,0,0))
    df.dateFromString("09.08.2017") should be (new DateTime(2017,8,9,0,0))
    df.dateFromString("09-08-2017") should be (new DateTime(2017,8,9,0,0))
  }

  it should "cause it to throw an exception if not"+
    " in any of the formats above" in {
    an [IllegalArgumentException] should be thrownBy df.dateFromString("01&01&2001")
  }
}
