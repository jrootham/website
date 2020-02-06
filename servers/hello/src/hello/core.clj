(ns hello.core
	(:gen-class)
	(:require [ring.adapter.jetty :refer [run-jetty]])
	(:require [ring.middleware.params :refer [wrap-params]])
	(:require [compojure.core :refer [defroutes GET]])
	(:require [compojure.route :refer [not-found]])
	(:require [clojure.string :as str])
	(:require [ring-debug-logging.core :refer [wrap-with-logger]])
	(:require [hiccup.page :refer [html5]])
)

(defn head []
	[:head [:title "Hello"]]
)

(defn body [name]
	[:body [:div (str "Hello " name)]]
)

(defn reply [name]
	(println "In reply")
	(html5 (head) (body name))
)

(defroutes replying
	(GET "/servers/hello/" [name] (reply name))
	(GET "/favicon.ico" [] {:status 404})
	(not-found {:status 404 :body "Not Found"})
)

(defn wrapper []
	(-> replying
		(wrap-params)
		(wrap-with-logger)
	)
)

(defn -main
  	"Hello"
  	[& args]
  	(if (== 1 (count args))
		(try
			(let [port (Integer/parseInt (first args))]
				(run-jetty (wrapper) {:host "127.0.0.1" :port port})
			)
			(catch NumberFormatException exception 
				(println (str args[0] " is not an int"))
			)
		)
	  	(println "Usage: hello port")
	)  	
)

