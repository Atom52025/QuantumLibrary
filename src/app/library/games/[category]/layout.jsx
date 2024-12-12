import { Checkbox, CheckboxGroup, Divider } from '@nextui-org/react';

import Link from 'next/link';

import CategorySection from '@/app/components/sections/CategorySection';
import { getServerSession } from 'next-auth/next';

export default async function GamesLayout({ children }) {
  return <section className="h-full w-full flex md:flex-row flex-col relative"> {children} </section>;
}
