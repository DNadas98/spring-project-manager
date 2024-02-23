import {ApiResponseBodyDto} from "./ApiResponseBodyDto.ts";

export interface PublicFetchResponseDto {
    httpResponse: Response,
    responseObject: ApiResponseBodyDto
}
