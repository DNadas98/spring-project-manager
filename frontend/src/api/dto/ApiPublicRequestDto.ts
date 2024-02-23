export interface ApiPublicRequestDto {
    readonly path: string;
    readonly method?: "GET" | "POST" | "PUT" | "PATCH" | "DELETE";
    readonly body?: object;
}
