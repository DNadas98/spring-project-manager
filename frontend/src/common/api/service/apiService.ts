import {ApiPublicRequestDto} from "../dto/ApiPublicRequestDto.ts";
import {ApiResponseDto} from "../dto/ApiResponseDto.ts";

export async function publicJsonFetch(request: ApiPublicRequestDto): Promise<ApiResponseDto> {
    try {
        const requestConfig: RequestInit = {
            method: `${request?.method ?? "GET"}`,
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "include"
        };
        if (request?.body) {
            requestConfig.body = JSON.stringify(request.body);
        }
        const baseUrl = import.meta.env.VITE_API_BASE_URL;
        const httpResponse: Response = await fetch(`${baseUrl}/${request.path}`, requestConfig);
        if (!httpResponse?.status) {
            throw new Error("Invalid response received from the server");
        }
        if (httpResponse?.headers?.get("Content-Type") !== "application/json") {
            throw new Error("Server response received in invalid format");
        }
        const responseObject: ApiPublicRequestDto = await httpResponse?.json();
        return {status: httpResponse?.status ?? 500, ...responseObject};
    } catch (e) {
        console.error("Failed to load requested resource");
        return {
            status: 500,
            error: "An unknown error has occurred"
        };
    }
}
