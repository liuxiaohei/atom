package org.atom.constant

/**
  * 正则表达式
  */
object Regexs {

  val emoji表情 = ".*(?:[\\uD83C\\uDF00-\\uD83D\\uDDFF]|[\\uD83E\\uDD00-\\uD83E\\uDDFF]|[\\uD83D\\uDE00-\\uD83D\\uDE4F]|[\\uD83D\\uDE80-\\uD83D\\uDEFF]|[\\u2600-\\u26FF]\\uFE0F?|[\\u2700-\\u27BF]\\uFE0F?|\\u24C2\\uFE0F?|[\\uD83C\\uDDE6-\\uD83C\\uDDFF]{1,2}|[\\uD83C\\uDD70\\uD83C\\uDD71\\uD83C\\uDD7E\\uD83C\\uDD7F\\uD83C\\uDD8E\\uD83C\\uDD91-\\uD83C\\uDD9A]\\uFE0F?|[\\u0023\\u002A\\u0030-\\u0039]\\uFE0F?\\u20E3|[\\u2194-\\u2199\\u21A9-\\u21AA]\\uFE0F?|[\\u2B05-\\u2B07\\u2B1B\\u2B1C\\u2B50\\u2B55]\\uFE0F?|[\\u2934\\u2935]\\uFE0F?|[\\u3030\\u303D]\\uFE0F?|[\\u3297\\u3299]\\uFE0F?|[\\uD83C\\uDE01\\uD83C\\uDE02\\uD83C\\uDE1A\\uD83C\\uDE2F\\uD83C\\uDE32-\\uD83C\\uDE3A\\uD83C\\uDE50\\uD83C\\uDE51]\\uFE0F?|[\\u203C\\u2049]\\uFE0F?|[\\u25AA\\u25AB\\u25B6\\u25C0\\u25FB-\\u25FE]\\uFE0F?|[\\u00A9\\u00AE]\\uFE0F?|[\\u2122\\u2139]\\uFE0F?|\\uD83C\\uDC04\\uFE0F?|\\uD83C\\uDCCF\\uFE0F?|[\\u231A\\u231B\\u2328\\u23CF\\u23E9-\\u23F3\\u23F8-\\u23FA]\\uFE0F?).*"

  val 数字 = "^[0-9]*$"

  val 汉字 = "^[\\u4e00-\\u9fa5]{0,}$"

  val 英文和数字 = "^[A-Za-z0-9]+$"

  val 由26个英文字母组成的字符串 = "^[A-Za-z]+$"

  val 由26个大写英文字母组成的字符串 = "^[A-Z]+$"

  val 由26个小写英文字母组成的字符串 = "^[a-z]+$"

  val 由数字和26个英文字母组成的字符串 = "^[A-Za-z0-9]+$"

  val 由数字_26个英文字母或者下划线组成的字符串 = "^\\w+$"

  val 中文英文_数字包括下划线 = "^[\\u4E00-\\u9FA5A-Za-z0-9_]+$"

  val 中文_英文_数字但不包括下划线等符号 = "^[\\u4E00-\\u9FA5A-Za-z0-9]+$ 或 ^[\\u4E00-\\u9FA5A-Za-z0-9]{2,20}$"

  val Email地址 = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"

  val 域名 = "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(/.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+/.?"

  val InternetURL = "[a-zA-z]+://[^\\s]* 或 ^http://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$"

  val 手机号码 = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$"

  val 电话号码 = "^(\\(\\d{3,4}-)|\\d{3.4}-)?\\d{7,8}$"

  val 国内电话号码 = "\\d{3}-\\d{8}|\\d{4}-\\d{7}"

  val 身份证号 = "^\\d{15}|\\d{18}$"

}
