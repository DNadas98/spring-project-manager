import {ApiPublicRequestDto} from "../dto/ApiPublicRequestDto.ts";

export async function publicFetch(request: ApiPublicRequestDto) {
    const requestConfig: RequestInit = {
        method: `${request?.method ?? "GET"}`,
        headers: {
            "Content-Type": "application/json"
        }
    };
    if (request?.body) {
        requestConfig.body = JSON.stringify(request.body);
    }
    const baseUrl = import.meta.env.VITE_API_BASE_URL;
    const httpResponse: Response = await fetch(`${baseUrl}/${request.path}`, requestConfig);
    const responseObject: ApiPublicRequestDto = await httpResponse?.json();
    return {httpResponse, responseObject};
}
