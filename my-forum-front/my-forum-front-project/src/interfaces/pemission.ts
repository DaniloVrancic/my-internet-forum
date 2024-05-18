export interface Permission{
    id: number | null,
    user_id: number,
    topic_id: number,
    topic_name: string,
    type: string;
}