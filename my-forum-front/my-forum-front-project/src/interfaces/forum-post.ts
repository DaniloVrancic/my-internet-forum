export interface ForumPost{ //Determines the interface for a ForumPost on my forum
    id: number,
    title: string,
    content: string,
    created_at: string,
    modified_at: string,
    topic: string,
    user_creator: string,
    status: string
}