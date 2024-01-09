//(min 3, max 20 characters, alphanumeric and underscores only)
export const usernameRegex = /^[a-zA-Z0-9_]{3,20}$/;
//(common simplified pattern for emails, permissive for the local part)
export const emailRegex = /^[^\s@]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,100}$/;
//(min 8, max 30 characters, allows special characters)
export const passwordRegex = /^[\s\S]{8,30}$/;
