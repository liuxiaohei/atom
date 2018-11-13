package org.atom.functions

import java.util
import java.util.function.{Consumer, Function}
import java.util.{Base64, Objects, Optional}

import com.google.gson.GsonBuilder

/*
 * @author ld
 * 基础工具函数仓库集合
 */
trait BaseTrait {
  // 随机数
  def random(a:Int):Int = scala.util.Random.nextInt(a)
  def nullToDefault[T >:Object](obj:T,default:T):T = Some(obj).getOrElse(default)
  def nullToZero(num:Integer):Integer = Some(num).getOrElse(0)
  def zero2Null(num:Integer):Integer = Some(num).filter(e => 0 != e).orNull
  def null2One(num:Integer):Integer = Some(num).filter(Objects.nonNull).getOrElse(1)
  def null2One(num:java.lang.Byte):java.lang.Byte = Some(num).filter(Objects.nonNull).getOrElse(1.toByte)
  def null2False(c:java.lang.Boolean):java.lang.Boolean = Some(c).filter(Objects.nonNull).getOrElse(false)
  def null2True(c:java.lang.Boolean):java.lang.Boolean = Some(c).filter(Objects.nonNull).getOrElse(true)
  def whenThrow(condition:Boolean,message:String):Unit = if(condition) throw error(message)
  def error(message:String):RuntimeException = new RuntimeException(message)
  def isNull(obj:Object):Boolean = Objects.isNull(obj)
  def nonNull(obj:Object):Boolean = Objects.nonNull(obj)
  def hasNotNull(o:util.List[Object]):Boolean = o.stream().anyMatch(nonNull)
  def allIsNull(o: util.List[Object]):Boolean = !hasNotNull(o)
  def isValidId(id:Integer):Boolean = Some(id).filter(nonNull).exists(e => e > 0)
  def isInValidId(id:Integer):Boolean = !isValidId(id)
  def isZero(num: Integer):Boolean = Some(num).filter(nonNull).exists(e => e equals 0)
  def isZero(num :java.lang.Long):Boolean = Some(num).filter(nonNull).exists(e => e equals 0L)
  def nonZero(num:Integer):Boolean = !isZero(num)
  def nonZero(num :java.lang.Long):Boolean = !isZero(num)
  def isOne(num: Integer):Boolean = Some(num).filter(nonNull).exists(e => e equals 1)
  def isOne(num :java.lang.Long):Boolean = Some(num).filter(nonNull).exists(e => e equals 1L)
  def nonOne(num:Integer):Boolean = !isOne(num)
  def nonOne(num :java.lang.Long):Boolean = !isOne(num)
  def hasNotZero(l:util.List[Integer]):Boolean = l.stream().anyMatch(e => 0 != e)
  def allIsZero(l:util.List[Integer]):Boolean = !hasNotZero(l)
  def lessThanZero(num:Integer):Boolean = Some(num).filter(nonNull).exists(e => e < 0)
  def isEmpty[T](collection: util.Collection[T]):Boolean = isNull(collection) || collection.isEmpty
  def isNotEmpty[T](collection: util.Collection[T]):Boolean = !isEmpty(collection)
  def isEmpty (str: String):Boolean = isNull(str) || str.trim.length == 0
  def isNotEmpty(str: String):Boolean = !isEmpty(str)
  def obj2Json[T](t: T):String = BaseTrait.G.toJson(t)
  def obj2PrettyJson[T](t: T):String = BaseTrait.PRETTY_G.toJson(t)
  private def isEmptyJson(json: String):Boolean = isEmpty(json) || json.matches("\\{\\s*\\}")
  def json2Obj[T >: Null](json: String, c: Class[T]):T = Some(json).filter(e => !isEmptyJson(e)).map(e => BaseTrait.G.fromJson(json,c)).orNull
  def check(field: String, message: String):Unit = whenThrow(isEmpty(field),message)
  def check(field: Integer, message: String):Unit = whenThrow(isInValidId(field),message)
  def check(obj: Object, message: String):Unit = whenThrow(isNull(obj),message)
  def check(field: String):Unit = check(field,"参数错误")
  def check(field:Integer):Unit = check(field,"参数错误")
  def check(obj:Object):Unit = check(obj,"参数错误")
  def base64Encode[T >: Null](t: T):String = Base64.getEncoder.encodeToString(obj2Json(t).getBytes("ISO_8859_1"))
  def base64Decode[T >: Null](key: String, c: Class[T]):T = json2Obj(new String(Base64.getDecoder.decode(key), "ISO_8859_1"), c)
  def safeList[T](list: java.util.List[T]): java.util.List[T] = if (nonNull(list)) list else new util.ArrayList[T]()
  def whenNonNullDo[T](c: Consumer[T], value: T):Unit = Optional.ofNullable(value).ifPresent(c)
  def whenNonNullDo[T](c: Consumer[util.List[T]], list: util.List[T]):Unit = Optional.ofNullable(list).filter(e => isNotEmpty(e)).ifPresent(c)
  def whenValidIdDo(c: Consumer[Integer], id: Integer):Unit = Optional.ofNullable(id).filter(isValidId).ifPresent(c)
  def first[T >: Null]: Function[util.List[T], T] = (e: util.List[T]) => if(isNotEmpty(e)) e.get(0) else null
}

object BaseTrait {
  private val G = new GsonBuilder().create()
  private val PRETTY_G = new GsonBuilder().setPrettyPrinting().create()
}
