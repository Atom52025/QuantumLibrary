export const dynamic = 'force-dynamic'; // defaults to auto

export async function POST(url, token, body) {
  // Create Query String
  const baseUrl = 'http://localhost:8080/';
  const completeUrl = `${baseUrl}${url}`;

  console.log('completeUrl', completeUrl);
    console.log('token', token);

  // Fetch Data
  const res = await fetch(completeUrl, {
    method: "POST",
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
    cache: 'no-store',
    body: JSON.stringify(body),
  });
  if (!res.ok) {
    console.log('Failed to fetch data');
    throw new Error(`Failed to fetch data: ${res.status}`);
  }
  return res.json();
}
