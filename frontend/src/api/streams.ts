export interface Stream {
    streamer: string,
    streamerName?: string,
    liveSince: number,
    amountViewers: number,
    streamConfig: StreamConfig,
}

export interface StreamConfig {
    userId: string,
    title: string,
    description: string,
    category: string,
}

export interface User {
  id: string,
  username: string,
  email: string,
  password: string,
  createdAt: number,
  following: string[],
  followers: string[],
}

export async function fetchOnlineStreamers(): Promise<Stream[] | null> {
    const response = await fetch("http://localhost:7457/api/streams", {
        method: "GET"
    })

    if (!response.ok) {
        console.error("Error fetching online streamers")
        return null;
    }

    return response.json();
}

export async function fetchUser(username: string): Promise<User | null> {
  const response = await fetch("http://localhost:7457/api/users/" + username, {
    method: "GET"
  })

  if (!response.ok) {
    console.error("Error fetching user")
    return null;
  }

  return response.json();
}

export async function fetchStream(username: string): Promise<Stream | null> {
  const response = await fetch("http://localhost:7457/api/streams/" + username, {
    method: "GET"
  })

  if (!response.ok) {
    console.error("Error fetching stream")
    return null;
  }

  return response.json();
}

export async function fetchStreamConfig(userid: string): Promise<StreamConfig | null> {
  const response = await fetch("http://localhost:7457/api/stream-configs/" + userid, {
    method: "GET"
  })

  if (!response.ok) {
    console.error("Error fetching streamConfig")
    return null;
  }

  return response.json();
}
