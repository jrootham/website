(ns nopassword.core
	(:gen-class)
	(:require [ring.adapter.jetty :as r])
	(:require [ring.middleware.params :as p])
	(:require [compojure.core :as c])
	(:require [clojure.string :as str])
	(:require [ring-debug-logging.core :as d])
	(:require [hiccup.core :as h])
	(:require [hiccup.page :as hp])
	(:require [hiccup.form :as hf])
	(:require [clj-http.client :as cl])
	(:require [nopassword.stuff :as s])
)

(defn mail-config [from to subject body]
	{
		:oauth-token s/mail-key
		:content-type :applicaton/json
		:form-params
		{
			:personalizations[{:to [{:email to}]}]
			:from {:email from}
			:subject subject
			:content [{:type "text/html" :value body}]
		}
	}
)

(defn send-mail [from to subject body]
	(cl/post "https://api.sendgrid.com/v3/mail/send" (mail-config from to subject body))
)

(defn head []
	[:head [:title "No Password"]]
)

(defn text-input [text-name label-text value]
	(h/html [:div (hf/label text-name label-text) (hf/text-field text-name value)])
)

(defn prompt-form [name address error]
	(h/html 
		[
			:div
			(h/html [:div error]) 
			(text-input :name "Name " name) 
			(text-input :address "Address " address)
		]
	)
)

(defn prompt-body [name address error]
	(h/html [:body (h/html [:div (prompt-form name address error)])])
)

(defn prompt [name address error]
	(hp/html5 (head) (prompt-body name address error))
)

(c/defroutes replying
	(c/GET "/" [] (prompt "" "" ""))
	(c/GET "/favicon.ico" [] {:status 404})
)

(defn wrapper []
	(-> replying
		(p/wrap-params)
		(d/wrap-with-logger)
	)
)

(defn -main
  	"No password"
  	[& args]
  	(if (== 1 (count args))
		(try
			(let [port (Integer/parseInt (first args))]
				(r/run-jetty (wrapper) {:host "127.0.0.1" :port port})
			)
			(catch NumberFormatException exception 
				(println (str args[0] " is not an int"))
			)
		)
	  	(println "Usage: hello port")
	)  	
)

