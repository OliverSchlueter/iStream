export interface User {
    id: string
    username: string
    email: string
    password: string
    created_at: number
    followers: string[]
    following: string[]
}