(ns su.myexe.kit.ds.IDS)

(defprotocol IDS
  "Datasource protocol."
  (get-data
    [this db]
    [this db record-key]
    [this db record-key value-key])

  (set-data
    [this db map-of-records]
    [this db record-key record]
    [this db record-key value-key value])

  (clear-data
    [this db])

  (get-errors
    [this db]
    [this db record-key]
    [this db record-key value-key])

  (set-errors
    [this db errors]
    [this db record-key errors]
    [this db record-key value-key errors])

  (clear-errors
    [this db]))