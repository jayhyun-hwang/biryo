var fonts = "@font-face{font-family:KaTeX_AMS;src:url(fonts/KaTeX_AMS-Regular.eot);src:url(fonts/KaTeX_AMS-Regular.eot#iefix) format('embedded-opentype'),url(fonts/KaTeX_AMS-Regular.woff2) format('woff2'),url(fonts/KaTeX_AMS-Regular.woff) format('woff'),url(fonts/KaTeX_AMS-Regular.ttf) format('truetype');font-weight:400;font-style:normal}@font-face{font-family:KaTeX_Caligraphic;src:url(fonts/KaTeX_Caligraphic-Bold.eot);src:url(fonts/KaTeX_Caligraphic-Bold.eot#iefix) format('embedded-opentype'),url(fonts/KaTeX_Caligraphic-Bold.woff2) format('woff2'),url(fonts/KaTeX_Caligraphic-Bold.woff) format('woff'),url(fonts/KaTeX_Caligraphic-Bold.ttf) format('truetype');font-weight:700;font-style:normal}@font-face{font-family:KaTeX_Caligraphic;src:url(fonts/KaTeX_Caligraphic-Regular.eot);src:url(fonts/KaTeX_Caligraphic-Regular.eot#iefix) format('embedded-opentype'),url(fonts/KaTeX_Caligraphic-Regular.woff2) format('woff2'),url(fonts/KaTeX_Caligraphic-Regular.woff) format('woff'),url(fonts/KaTeX_Caligraphic-Regular.ttf) format('truetype');font-weight:400;font-style:normal}@font-face{font-family:KaTeX_Fraktur;src:url(fonts/KaTeX_Fraktur-Bold.eot);src:url(fonts/KaTeX_Fraktur-Bold.eot#iefix) format('embedded-opentype'),url(fonts/KaTeX_Fraktur-Bold.woff2) format('woff2'),url(fonts/KaTeX_Fraktur-Bold.woff) format('woff'),url(fonts/KaTeX_Fraktur-Bold.ttf) format('truetype');font-weight:700;font-style:normal}@font-face{font-family:KaTeX_Fraktur;src:url(fonts/KaTeX_Fraktur-Regular.eot);src:url(fonts/KaTeX_Fraktur-Regular.eot#iefix) format('embedded-opentype'),url(fonts/KaTeX_Fraktur-Regular.woff2) format('woff2'),url(fonts/KaTeX_Fraktur-Regular.woff) format('woff'),url(fonts/KaTeX_Fraktur-Regular.ttf) format('truetype');font-weight:400;font-style:normal}@font-face{font-family:KaTeX_Main;src:url(fonts/KaTeX_Main-Bold.eot);src:url(fonts/KaTeX_Main-Bold.eot#iefix) format('embedded-opentype'),url(fonts/KaTeX_Main-Bold.woff2) format('woff2'),url(fonts/KaTeX_Main-Bold.woff) format('woff'),url(fonts/KaTeX_Main-Bold.ttf) format('truetype');font-weight:700;font-style:normal}@font-face{font-family:KaTeX_Main;src:url(fonts/KaTeX_Main-Italic.eot);src:url(fonts/KaTeX_Main-Italic.eot#iefix) format('embedded-opentype'),url(fonts/KaTeX_Main-Italic.woff2) format('woff2'),url(fonts/KaTeX_Main-Italic.woff) format('woff'),url(fonts/KaTeX_Main-Italic.ttf) format('truetype');font-weight:400;font-style:italic}@font-face{font-family:KaTeX_Main;src:url(fonts/KaTeX_Main-Regular.eot);src:url(fonts/KaTeX_Main-Regular.eot#iefix) format('embedded-opentype'),url(fonts/KaTeX_Main-Regular.woff2) format('woff2'),url(fonts/KaTeX_Main-Regular.woff) format('woff'),url(fonts/KaTeX_Main-Regular.ttf) format('truetype');font-weight:400;font-style:normal}@font-face{font-family:KaTeX_Math;src:url(fonts/KaTeX_Math-Italic.eot);src:url(fonts/KaTeX_Math-Italic.eot#iefix) format('embedded-opentype'),url(fonts/KaTeX_Math-Italic.woff2) format('woff2'),url(fonts/KaTeX_Math-Italic.woff) format('woff'),url(fonts/KaTeX_Math-Italic.ttf) format('truetype');font-weight:400;font-style:italic}@font-face{font-family:KaTeX_SansSerif;src:url(fonts/KaTeX_SansSerif-Regular.eot);src:url(fonts/KaTeX_SansSerif-Regular.eot#iefix) format('embedded-opentype'),url(fonts/KaTeX_SansSerif-Regular.woff2) format('woff2'),url(fonts/KaTeX_SansSerif-Regular.woff) format('woff'),url(fonts/KaTeX_SansSerif-Regular.ttf) format('truetype');font-weight:400;font-style:normal}@font-face{font-family:KaTeX_Script;src:url(fonts/KaTeX_Script-Regular.eot);src:url(fonts/KaTeX_Script-Regular.eot#iefix) format('embedded-opentype'),url(fonts/KaTeX_Script-Regular.woff2) format('woff2'),url(fonts/KaTeX_Script-Regular.woff) format('woff'),url(fonts/KaTeX_Script-Regular.ttf) format('truetype');font-weight:400;font-style:normal}@font-face{font-family:KaTeX_Size1;src:url(fonts/KaTeX_Size1-Regular.eot);src:url(fonts/KaTeX_Size1-Regular.eot#iefix) format('embedded-opentype'),url(fonts/KaTeX_Size1-Regular.woff2) format('woff2'),url(fonts/KaTeX_Size1-Regular.woff) format('woff'),url(fonts/KaTeX_Size1-Regular.ttf) format('truetype');font-weight:400;font-style:normal}@font-face{font-family:KaTeX_Size2;src:url(fonts/KaTeX_Size2-Regular.eot);src:url(fonts/KaTeX_Size2-Regular.eot#iefix) format('embedded-opentype'),url(fonts/KaTeX_Size2-Regular.woff2) format('woff2'),url(fonts/KaTeX_Size2-Regular.woff) format('woff'),url(fonts/KaTeX_Size2-Regular.ttf) format('truetype');font-weight:400;font-style:normal}@font-face{font-family:KaTeX_Size3;src:url(fonts/KaTeX_Size3-Regular.eot);src:url(fonts/KaTeX_Size3-Regular.eot#iefix) format('embedded-opentype'),url(fonts/KaTeX_Size3-Regular.woff2) format('woff2'),url(fonts/KaTeX_Size3-Regular.woff) format('woff'),url(fonts/KaTeX_Size3-Regular.ttf) format('truetype');font-weight:400;font-style:normal}@font-face{font-family:KaTeX_Size4;src:url(fonts/KaTeX_Size4-Regular.eot);src:url(fonts/KaTeX_Size4-Regular.eot#iefix) format('embedded-opentype'),url(fonts/KaTeX_Size4-Regular.woff2) format('woff2'),url(fonts/KaTeX_Size4-Regular.woff) format('woff'),url(fonts/KaTeX_Size4-Regular.ttf) format('truetype');font-weight:400;font-style:normal}@font-face{font-family:KaTeX_Typewriter;src:url(fonts/KaTeX_Typewriter-Regular.eot);src:url(fonts/KaTeX_Typewriter-Regular.eot#iefix) format('embedded-opentype'),url(fonts/KaTeX_Typewriter-Regular.woff2) format('woff2'),url(fonts/KaTeX_Typewriter-Regular.woff) format('woff'),url(fonts/KaTeX_Typewriter-Regular.ttf) format('truetype');font-weight:400;font-style:normal}"
var fontsR = new RegExp('fonts/', 'g')
fonts = fonts.replace(fontsR, contentHref);
var ns = document.createElement('style');
ns.appendChild(document.createTextNode(fonts));
document.head.appendChild(ns);
