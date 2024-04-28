import {User} from "./types";

export async function getUserById(id: string): Promise<User> {
    const response = await fetch('http://localhost:8080/v1/users/' + id, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    })

    if (!response.ok || response.status !== 200) {
        throw new Error('Failed to get user: ' + await response.text())
    }

    const data = await response.json()

    return {
        id: data.id,
        username: data.username,
        email: data.email,
        password: data.password,
        created_at: data.created_at,
        followers: data.followers,
        following: data.following,
    }
}