import {UserAccountType} from "./UserAccountType.ts";

export interface UserAccountDto {
  readonly id: number;
  readonly email: string;
  readonly accountType: UserAccountType;
}
