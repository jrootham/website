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


(defn hello []
;	(cl/get "https://jrootham.ca/server/hello" {:query-params "Foo"})
	(cl/get "https://jrootham.ca/hello")
)

; curl --request POST \
;   --url https://api.sendgrid.com/v3/mail/send \
;   --header "Authorization: Bearer $SENDGRID_API_KEY" \
;   --header 'Content-Type: application/json' \
;   --data '{"personalizations": [{"to": [{"email": "jrootham@gmail.com"}]}],
; 		  	"from": {"email": "test@example.com"},
; 		  	"subject": "Sending with SendGrid is Fun",
; 		  	"content": [{"type": "text/plain", "value": "and easy to do anywhere, even with cURL"}]
; 		  }'


(defn mail-config [from to subject body]
	{
		:authorization (str "Bearer " s/mail-key)
		:content-type :applicaton/json
		:form-params
		{
			:personalizations
				[
					{
						:to 
						[
							{
								:email to
							}
						]
					}
				]
			:from 
				{
					:email from
				}
			:subject subject
			:content body
		}

		:debug true
		:debug-body true
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

