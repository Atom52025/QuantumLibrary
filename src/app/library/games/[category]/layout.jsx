import { Checkbox, CheckboxGroup, Divider } from '@nextui-org/react';

import Link from 'next/link';

import { getServerSession } from 'next-auth/next';
import CategorySection from "@/app/components/sections/CategorySection";

export default async function GamesLayout({ children }) {
  return <section className="h-full w-full flex lg:flex-row flex-col relative"> {children} </section>
}
