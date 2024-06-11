export interface Stream {
    streamer: string,
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

export async function fetchOnlineStreamers(): Promise<any> {
    const response = await fetch("http://localhost:8080/api/streams", {
        method: "GET"
    })

    if (!response.ok) {
        console.error("Error fetching online streamers")
        return null;
    }

    return response.json();
}