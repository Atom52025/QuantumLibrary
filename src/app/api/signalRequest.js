export const dynamic = 'force-dynamic'; // defaults to auto

export async function GET(url, token, signal) {
  // Create Query String
  const baseUrl = 'http://localhost:8080/';
  const completeUrl = `${baseUrl}${url}`;

  // Fetch Data
  const res = await fetch(completeUrl, {
    signal,
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  if (!res.ok) {
    console.log('Failed to fetch data');
  }
  return res.json();
}
