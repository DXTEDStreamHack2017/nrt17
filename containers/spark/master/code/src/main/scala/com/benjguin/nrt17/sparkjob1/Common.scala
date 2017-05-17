package com.benjguin.nrt17.sparkjob1

import java.util.Date
import java.util.Calendar
import java.sql.Timestamp
import java.text.{DateFormat, SimpleDateFormat}

object Commons {

  case class UserEvent(device_id: String, category:String, window_time:String, m1_sum_downstream: String, m2_sum_downstream: String)
      extends Serializable
}