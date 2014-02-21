(ns clarango.collection-ops
  (:require [clarango.document :as document]))

;;; some experimental clojure-like collection methods, that use the methods of clarango.document under the covers

(defn cla-assoc!
  "Adds one document (val) to a collection (specified by collection-name) with a given key.
  Always uses the default database set in clarango.core.

  Modeled on core/assoc (http://clojuredocs.org/clojure_core/clojure.core/assoc)
  Does the same, just on an ArangoDB collection. The difference is that you can currently only pass one key and one document
  to add to the collection, not several like in clojure.core/dissoc"
  [collection-name key val]
  (document/create (merge val {:_key key}) collection-name {"createCollection" false, "waitForSync" true}))

(defn cla-dissoc!
  "Removes a document thats identified by the key parameter from a collection.

  Modeled on core/dissoc (http://clojuredocs.org/clojure_core/clojure.core/dissoc)
  Does the same, just on an ArangoDB collection. The difference is that you can currently only pass one key to remove
  from the collection, not several like in clojure.core/dissoc"
  [collection-name key]
  (document/delete-by-key key collection-name {"waitForSync" true}))

(defn cla-conj!
  "Adds one document (x) to a collection (specified by collection-name). The key for the document is generated by ArangoDB.
  Always uses the default database set in clarango.core.

  Modeled on core/conj (http://clojuredocs.org/clojure_core/clojure.core/conj)
  Does the same, just on an ArangoDB collection. The difference is that you can currently only pass one element to add
  to the collection, not several like in clojure.core/conj"
  [collection-name x]
  (document/create x collection-name {"createCollection" false, "waitForSync" true}))

(defn cla-get!
  "Gets a document out of a collection by key.

  Modeled on core/get (http://clojuredocs.org/clojure_core/clojure.core/get)
  Does the same, just on an ArangoDB collection.

  Currently this method throws an error when used with a key that does not exist. This should be changed in the future, 
  also it should be possible to give a value that is returned by the function, in case the key does not exist."
  [collection-name key]
  (document/get-by-key key collection-name))

;; Frage:

;; was soll der Rückgabewert sein? Wenn jedes mal die ganze Collection zurückgegeben wird, müsste noch
;; eine collection/get-all-documents Anfrage gesendet werden; wäre suboptimal, da extra Request und damit nicht mehr performant;

;; also stellt sich die Frage, ob das ganze Unternehmen überhaupt Sinn macht, da sich die Methoden in drei 
;; grundsätzlichen Dingen unterscheiden:
;; 1. Veränderung am Zustand der DB
;; 2. Rückgabewert
;; 3. es wird nicht das Collection Objekt selber übergeben sondern der Name