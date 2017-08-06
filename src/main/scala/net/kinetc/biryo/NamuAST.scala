package net.kinetc.biryo

import scala.collection.Seq
import HTMLRenderer._

// TODO: instantiate Applicative fmap function of NamuAST
object NamuAST {
  type NamuMap = PartialFunction[NamuMark, NamuMark]
  // s"\"" -> s"$q"  (Build Error???)
  val q = '"'

  trait NamuMark {
    def mkString: String = ""

    /**
      * Child-First Traversing
      */
    def cfs(f: NamuMark => Unit): Unit = f(this)

    /**
      * Node-First Traversing
      */
    def nfs(f: NamuMark => Unit): Unit = f(this)

    /**
      * Child-First Node Mapping
      * @param f: PartialFunction, don't need to apply it to child
      * @return default: this, or f.apply(this)
      */
    def cfsMap(f: NamuMap): NamuMark = if (f.isDefinedAt(this)) f(this) else this

    /**
      * Node-First Node Mapping
      * @param f PartialFunction, You should apply it to child when `this` is Paragraph or DocLink
      * @return default: this, or f.apply(this)
      */
    def nfsMap(f: NamuMap): NamuMark = cfsMap(f)
  }

  trait HasNamu extends NamuMark {
    val value: NamuMark
    def constructor(nm: NamuMark): NamuMark

    override def cfs(f: (NamuMark) => Unit) = { value.cfs(f); f(this) }
    override def nfs(f: (NamuMark) => Unit) = { f(this); value.nfs(f) }

    override def cfsMap(f: NamuMap): NamuMark = {
      val childMap = value.cfsMap(f)
      val newThis = constructor(childMap)
      if (f.isDefinedAt(newThis)) f(newThis) else newThis
    }
    override def nfsMap(f: NamuMap): NamuMark = {
      if (f.isDefinedAt(this)) {
        val newThis = f(this)
        newThis match {
          case t: HasNamu => t.constructor(t.value.nfsMap(f))
          case _ => newThis
        }
      } else {
        constructor(value.nfsMap(f))
      }
    }
  }

  trait HasHref {
    val href: NamuHref
    def hrefConstructor(href: NamuHref): NamuMark
    def hrefMap(f: NamuHref => NamuHref): NamuMark = hrefConstructor(f(href))
  }

  ////// ------ Paragraph ------- ///////

  /**
    * List of NamuMark Objects (`Seq[NamuMark]` Wrapper)
    * @param valueSeq a sequence of NamuMark Objects
    */
  case class Paragraph(valueSeq: Seq[NamuMark]) extends NamuMark {
    override def mkString =
      valueSeq.foldLeft(new StringBuilder)((sb, nm) => sb.append(nm.mkString)).toString
    override def cfs(f: (NamuMark) => Unit) = { valueSeq.foreach(_.cfs(f)); f(this) }
    override def nfs(f: (NamuMark) => Unit) = { f(this); valueSeq.foreach(_.nfs(f)) }
    override def cfsMap(f: NamuMap) = {
      val childMap = valueSeq.map(_.cfsMap(f))
      val newThis = Paragraph(childMap)
      if (f.isDefinedAt(newThis)) f(newThis) else newThis
    }
    override def nfsMap(f: NamuMap): NamuMark =
      if (f.isDefinedAt(this)) f(this) else Paragraph(valueSeq.map(_.nfsMap(f)))
  }

  case class ParagraphBuilder(markList: Seq[NamuMark], sb: StringBuilder) extends NamuMark

  ////// ------ Indent & Lists ------ //////

  case class ListObj(value: NamuMark, listType: ListType, indentSize: Int) extends HasNamu {
    def constructor(nm: NamuMark) = ListObj(nm, listType, indentSize)
  }

  sealed trait ListType
  // * star -> <ul> ~~ </ul>
  case object Type_star extends ListType
  // 1.#42 -> <ol type="1" start="42"> ~~ </ol>
  case class Type_1(offset: Int = 1) extends ListType
  // i.#42 -> <ol type="i" start="42"> ~~ </ol>
  case class Type_i(offset: Int = 1) extends ListType
  // I.#42 -> <ol type="I" start="42"> ~~ </ol>
  case class Type_I(offset: Int = 1) extends ListType
  // a.#42 -> <ol type="a" start="42"> ~~ </ol>
  case class Type_a(offset: Int = 1) extends ListType
  // A.#42 -> <ol type="A" start="42"> ~~ </ol>
  case class Type_A(offset: Int = 1) extends ListType


  ////// ------ Table ------ //////



  ////// ------- BlockQuote ------ //////

  case class BlockQuote(value: NamuMark) extends HasNamu {
    override def mkString = s"<blockquote><div ${c(indentClass)}>${value.mkString}</div></blockquote>"
    def constructor(nm: NamuMark) = BlockQuote(nm)
  }

  ////// ------ Single Bracket FootNote / Macros ------ //////

  case class FootNote(value: NamuMark, noteStr: Option[String]) extends HasNamu {
    override def mkString = noteStr match {
      case Some(s) => s"<a name=${q}r$s$q></a><a href=${q}entry://#$s$q>[$s]</a>"
      case None => s"<a name=${q}rWTF$q></a><a href=${q}entry://#WTF$q>[*]</a>"
    }
    def constructor(nm: NamuMark) = FootNote(nm, noteStr)
  }

  // TODO: Calculate This!
  case class AgeMacro(date: String) extends NamuMark {
    override def mkString = s"(${date}로부터 나이)"
  }
  case object DateMacro extends NamuMark {
    // do not calculate it for MDict
    override def mkString = "[현재 시간]"
  }
  // TODO: Render This From HTMLRenderer
  case object FootNoteList extends NamuMark {
    override def mkString = "[각주]"
  }

  case object TableOfContents extends NamuMark {
    override def mkString = "[목차]"
  }
  // TODO: Should we render this??
  case class Include(rawHref: String, args: Map[String, String]) extends NamuMark {
    override def mkString = {
      if (args.isEmpty) {
        s"[include(<a href=${q}entry://$rawHref$q>$rawHref</a>)]"
      } else {
        val argString = args.mkString(", ").replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;")
        s"[include(<a href=${q}entry://$rawHref$q>$rawHref</a>), args:$argString)]"
      }
    }
  }
  case class Anchor(anchor: String) extends NamuMark {
    override def mkString = s"<a name=$q$anchor$q></a>"
  }

  case class YoutubeLink(id: String, args: Map[String, String]) extends NamuMark {
    // Fallback to Link (For MDict)
    override def mkString = s"<a href=${q}entry://https://www.youtube.com/watch?v=$id$q>[유튜브 링크]</a>"
  }

  ////// ------ Double Bracket Links ------ //////

  // [[파일:$href|$htmlOption]]
  case class FileLink(href: String, htmlOption: Map[String, String]) extends NamuMark {
    // Fallback to Link (for Mdict)
    override def mkString = s"<a href=${q}entry://$href$q>[파일:$href]</a>"
  }
  // [[분류:$docType]]
  case class DocType(docType: String) extends NamuMark {
    override def mkString = s"<div ${c(docTypeClass)}>" +
      s"분류: <a href=${q}entry://분류:$docType$q>$docType</a></div>"
  }
  // [[$href|$alias]] -> href will be changed to NormalHref after the postprocessing
  case class DocLink(href: NamuHref, alias: Option[NamuMark]) extends HasNamu with HasHref {
    override def mkString = alias match {
      case Some(nm) => s"<a href=$q${href.value}$q>${nm.mkString}</a>"
      case None => s"<a href=$q${href.value}$q>${href.value}</a>"
    }
    val value: NamuMark = alias.orNull
    override def constructor(nm: NamuMark) = DocLink(href, Some(nm))

    override def cfs(f: (NamuMark) => Unit) = { alias.foreach(_.cfs(f)); f(this) }
    override def nfs(f: (NamuMark) => Unit) = { f(this); alias.foreach(_.nfs(f)) }
    override def cfsMap(f: NamuMap) = {
      val childMap = alias.map(_.cfsMap(f))
      val newThis = DocLink(href, childMap)
      if (f.isDefinedAt(newThis)) f(newThis) else newThis
    }
    override def nfsMap(f: NamuMap): NamuMark =
      if (f.isDefinedAt(this)) f(this) else DocLink(href, alias.map(_.nfsMap(f)))
    def hrefConstructor(href: NamuHref): NamuMark = DocLink(href, alias)
  }

  ////// ------ Curly Brace Blocks ------ //////

  // {{{#!syntax $language $value}}}
  case class SyntaxBlock(language: String, value: String) extends NamuMark {
    override def mkString = s"<pre><code>$value</code></pre>"
  }
  // {{{$!wiki style="$style" $value}}}
  case class WikiBlock(style: String, value: NamuMark) extends NamuMark {
    override def mkString = s"<div style=$q$style$q>${value.mkString}</div>"
  }
  // {{|$value|}}
  case class WordBox(value: NamuMark) extends NamuMark with HasNamu {
    override def mkString = s"<table ${c(wordBoxClass)}><tbody><tr><td><p>${value.mkString}</p></td></tr></tbody></table>"
    def constructor(nm: NamuMark) = WordBox(nm)
  }
  case class SizeBlock(value: NamuMark, size: Int) extends HasNamu {
    override def mkString = s"<font size=$q+$size$q>${value.mkString}</font>"
    def constructor(nm: NamuMark) = SizeBlock(nm, size)
  }
  case class ColorBlock(value: NamuMark, color: String) extends HasNamu {
    override def mkString = s"<font color=$q$color$q>${value.mkString}</font>"
    def constructor(nm: NamuMark) = ColorBlock(nm, color)
  }

  ////// ------ One-Liners ------- //////

  // ##Comment -> Comment("Comment")
  case class Comment(value: String) extends NamuMark

  // === Heading === -> RawHeadings(RawString("Heading"), 3)
  case class RawHeadings(value: NamuMark, size: Int) extends HasNamu {
    override def mkString = s"<h$size>${value.mkString}</h$size><hr>"
    def constructor(nm: NamuMark) = RawHeadings(nm, size)
  }

  // Post Process Only AST Node
  case class Headings(value: NamuMark, no: Seq[Int]) extends HasNamu {
    override def mkString = {
      val hsize = if (no.length <= 5) no.length + 1 else 6
      val hno = no.mkString(".")
      s"<h$hsize><a name=${q}s-$hno$q><a href=$q#headList$q>$hno. </a></a>${value.mkString}</h$hsize>"
    }
    def constructor(nm: NamuMark) = Headings(nm, no)
  }

  ////// ------ Span Marks ------ //////

  sealed trait SpanMark
  case class Strike(value: NamuMark) extends HasNamu with SpanMark {
    override def mkString = s"<del>${value.mkString}</del>"
    def constructor(nm: NamuMark) = Strike(nm)
  }
  case class Sup(value: NamuMark) extends HasNamu with SpanMark {
    override def mkString = s"<sup>${value.mkString}</sup>"
    def constructor(nm: NamuMark) = Sup(nm)
  }
  case class Sub(value: NamuMark) extends HasNamu with SpanMark {
    override def mkString = s"<sub>${value.mkString}</sub>"
    def constructor(nm: NamuMark) = Sub(nm)
  }
  case class Underline(value: NamuMark) extends HasNamu with SpanMark {
    override def mkString = s"<u>${value.mkString}</u>"
    def constructor(nm: NamuMark) = Underline(nm)
  }
  case class Bold(value: NamuMark) extends HasNamu with SpanMark {
    override def mkString = s"<b>${value.mkString}</b>"
    def constructor(nm: NamuMark) = Bold(nm)
  }
  case class Italic(value: NamuMark) extends HasNamu with SpanMark {
    override def mkString = s"<i>${value.mkString}</i>"
    def constructor(nm: NamuMark) = Italic(nm)
  }

  case class Redirect(value: String) extends NamuMark {
    override def mkString = s"<a href=${q}entry://$value$q>리다이렉트:$value</a>"
  }

  ////// ------ Basic Blocks ------- //////

  // HTML Unescaped String {{{#!html ... }}}
  case class HTMLString(value: String) extends NamuMark {
    override def mkString = value
  }
  // HTML Escaped Normal String
  case class RawString(value: String) extends NamuMark {
    override def mkString = value
  }
  // Markup Ignored String {{{ ... }}}
  case class InlineString(value: String) extends NamuMark {
    override def mkString = s"<code>$value</code>"
  }
  // [br] -> BR
  case object BR extends NamuMark {
    override def mkString = "<br>"
  }
  // ---- ~ ---------- (4 to 10 times)
  case object HR extends NamuMark {
    override def mkString = "<hr>"
  }

  ////// ------ href Trait ------ //////

  sealed trait NamuHref {
    val value: String
  }
  // [[value]] => NormalHref("value")
  case class NormalHref(value: String) extends NamuHref
  // [[value#s-0.1.2]] => ParaHref("value", Seq[Int](0, 1, 2))
  case class ParaHref(value: String, paraNo: Seq[Int]) extends NamuHref
  // [[value#anchor]] => AnchorHref("value", "anchor")
  case class AnchorHref(value: String, anchor: String) extends NamuHref
  // [[http://example.com]] => ExternalHref("http://example.com")
  case class ExternalHref(value: String) extends NamuHref
  // [[#1.4.1]] => SelfParaHref(Seq[Int](1,4,1))
  case class SelfParaHref(paraNo: Seq[Int]) extends NamuHref {
    val value: String = "#s-" + paraNo.mkString(".")
  }
  // [[#anchor]] => SelfAnchorHref("anchor")
  case class SelfAnchorHref(anchor: String) extends NamuHref {
    val value: String = "#" + anchor
  }
  // [[../]] => SuperDocHref
  case object SuperDocHref extends NamuHref {
    val value: String = "../"
  }
  // [[/child]] => ChildDocHref("child")
  case class ChildDocHref(childHref: NamuHref) extends NamuHref {
    val value: String = s"/${childHref.value}"
  }

  // 최대한 Paragraph라는 Seq[NamuMark] Wrapper를 줄이기
  private[biryo] def pbResolver(pb: ParagraphBuilder): NamuMark = {
    if (pb.markList.isEmpty) {
      RawString(pb.sb.toString)
    } else if (pb.sb.isEmpty) {
      if (pb.markList.length == 1)
        pb.markList.head
      else
        Paragraph(pb.markList)
    } else {
      Paragraph(pb.markList :+ RawString(pb.sb.toString))
    }
  }

  private[biryo] def pbMerger(pb: ParagraphBuilder, lineObj: NamuMark): ParagraphBuilder = {
    if (pb.sb.isEmpty) {
      pb.markList match {
        case (p: Paragraph) +: Seq() => ParagraphBuilder(p.valueSeq :+ lineObj, pb.sb)
        case _ => ParagraphBuilder(pb.markList :+ lineObj, pb.sb)
      }
    } else {
      ParagraphBuilder(
        pb.markList :+ RawString(pb.sb.toString) :+ lineObj,
        new StringBuilder
      )
    }
  }
}

