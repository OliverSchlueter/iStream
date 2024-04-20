# Types

## User

- id: string (uuid)
- username: string
- email: string
- password: string
- created_at: long (timestamp)
- following: string[] (user ids)
- followers: string[] (user ids)

## StreamConfig

- user_id: string
- title: string
- description: string
- language: string (country code)
- category: string
- tags: string[]

## Message

- streamer: string (user_id)
- sender: string (user_id)
- content: string
- sent_at: long (timestamp)

## Stream

- streamer: string (user_id)
- streamConfig: StreamConfig
- live_since: long (timestamp)
- amount_viewers: int