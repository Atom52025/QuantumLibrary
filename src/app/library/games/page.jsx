import { Checkbox, CheckboxGroup } from '@nextui-org/react';
import { signIn } from 'next-auth/react';

import Image from 'next/image';

import { authOptions } from '@/app/api/auth/[...nextauth]/authOptions';
import ContentFiltering from '@/app/components/ContentFiltering';
import { Button } from '@nextui-org/button';
import { getServerSession } from 'next-auth/next';

async function getData(category) {
  // Get Session
  const session = await getServerSession(authOptions);

  // Create Query String
  const baseUrl = 'http://localhost:8080/api/user/';
  const username = session.user.username;
  const categoryQuery = category != null ? `?category=${category}` : '';
  const url = `${baseUrl}${username}/games${categoryQuery}`;

  // Fetch Data
  const res = await fetch(url, {
    headers: {
      Authorization: `Bearer ${session.user.token}`,
    },
    cache: 'no-store',
  });
  if (!res.ok) {
    console.log('Failed to fetch data');
  }
  return res.json();
}

export default async function Page({ searchParams }) {
  const data = await getData(searchParams?.category);
  const tags = data?.games
    .map(({ game }) => game.tags.split(',').map((tag) => tag.trim()))
    .reduce((allTags, gameTags) => {
      gameTags.forEach((tag) => allTags.add(tag));
      return allTags;
    }, new Set());
  return <ContentFiltering data={data} tags={[...tags]} />;
}
