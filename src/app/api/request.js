export const dynamic = 'force-dynamic'; // defaults to auto

const baseUrl = 'http://localhost:8080/';

export async function GET(url) {
  // Create Query String
  const completeUrl = `${baseUrl}${url}`;

  // Fetch Data
  const res = await fetch(completeUrl, {
    cache: 'no-store',
  });
  if (!res.ok) {
    console.log('Failed to fetch data');
    return null;
  }
  return res.json();
}

export async function POST(url, body) {
  // Create Query String
  const completeUrl = `${baseUrl}${url}`;

  // Fetch Data
  const res = await fetch(completeUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
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

export async function DELETE(url) {
  // Create Query String
  const completeUrl = `${baseUrl}${url}`;

  // Fetch Data
  const res = await fetch(completeUrl, {
    method: 'DELETE',
    cache: 'no-store',
  });
  if (!res.ok) {
    console.log('Entity not found');
  }
}

export async function PATCH(url, body) {
  // Create Query String
  const completeUrl = `${baseUrl}${url}`;

  // Fetch Data
  const res = await fetch(completeUrl, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
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
