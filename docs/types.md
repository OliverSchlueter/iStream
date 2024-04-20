# Types

## User

id: string (uuid)
username: string
email: string
password: string
created_at: long (timestamp)
following: string[] (uuids)
followers: string[] (uuids)

## StreamConfig

user_id: string (uuid)
title: string
description: string
category: string
tags: string[]

## Chat

stream_id: string (a user_id)
user_id: string (who sent the message)
message: string
timestamp: long (time sent)