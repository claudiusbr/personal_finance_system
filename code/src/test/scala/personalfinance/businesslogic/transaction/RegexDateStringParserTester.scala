package personalfinance
package businesslogic
package transaction

import org.joda.time.DateTime

/**
  * integration tester for DateFormatter and DateTime
  */
class RegexDateStringParserTester extends BehaviourTester {
  val df = new RegexDateStringParser
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
    an [IllegalArgumentException] should be thrownBy df.dateFromString("31&12&2001")
    an [IllegalArgumentException] should be thrownBy df.dateFromString("12&31&2001")
    an [IllegalArgumentException] should be thrownBy df.dateFromString("2001&12&31")
    an [IllegalArgumentException] should be thrownBy df.dateFromString("31122001")
    an [IllegalArgumentException] should be thrownBy df.dateFromString("12312001")
    an [IllegalArgumentException] should be thrownBy df.dateFromString("20011231")
  }
}
