import {User} from "./types";

export async function getUserById(id: string): Promise<User> {
    const response = await fetch('http://localhost:8080/api/v1/users/' + id, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'AuthUser': 'test@email.de',
            'AuthPassword': '541fsd15ds4f15',
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

export async function createUser(user: User): Promise<void> {
    const response = await fetch('http://localhost:8080/api/v1/users/', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user)
    })

    if (!response.ok || response.status !== 200) {
        throw new Error('Failed to create user: ' + await response.text())
    }
}

async function use() {
    try {
        await createUser({
            id: '1',
            username: 'test',
            email: 'test@test.de',
            password: 'test',
            created_at: 0,
            followers: [],
            following: [],
        });
    } catch (e) {
        window.alert('Failed to create user.')
    }

    // ...
}