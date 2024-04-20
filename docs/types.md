# Types

## User

- id: string (uuid)
- username: string
- email: string
- password: string
- createdAt: long (timestamp)
- following: string[] (user ids)
- followers: string[] (user ids)

## StreamConfig

- userId: string
- title: string
- description: string
- language: string (country code)
- category: string
- tags: string[]

## Message

- streamer: string (user id)
- sender: string (user id)
- content: string
- sentAt: long (timestamp)

## Stream

- streamer: string (user id)
- streamConfig: StreamConfig
- liveSince: long (timestamp)
- amountViewers: int