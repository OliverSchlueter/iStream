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
- category: string

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