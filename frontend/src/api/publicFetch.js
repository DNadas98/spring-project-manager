export async function publicFetch(path, method = "GET", bodyObject = null) {
  const requestConfig = {
    method: `${method}`,
    headers: {
      "Content-Type": "application/json"
    }
  };
  if (bodyObject) {
    requestConfig.body = JSON.stringify(bodyObject);
  }
  const baseUrl = import.meta.env.VITE_API_BASE_URL;
  const httpResponse = await fetch(`${baseUrl}/${path}`, requestConfig);
  const responseObject = await httpResponse?.json();
  return {httpResponse, responseObject};
}
