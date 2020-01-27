(defproject hello "0.1.0"
	:description "Hello, world"
	:url "http://example.com/FIXME"
	:dependencies 
	[
		[org.clojure/clojure "1.8.0"]
		[ring/ring-jetty-adapter "1.8.0"]
		[compojure "1.6.1"]
		[hiccup "2.0.0-alpha2"]
		[bananaoomarang/ring-debug-logging "1.1.0"]
	]
	:main ^:skip-aot hello.core
	:target-path "target/%s"
	:profiles {:uberjar {:aot :all}}
)
