import { Card, CardBody, CardHeader, ScrollShadow } from '@nextui-org/react';

import Image from 'next/image';

export default async function Page() {
  return (
    <section className="h-full w-full flex flex-col items-center justify-center bg-gray-900 text-white relative">
      <div className="absolute h-full w-full flex items-center justify-center">
        <div className="aspect-square h-[80%] opacity-10 absolute ">
          <Image src="/images/QuantumLibrary.png" priority alt="Descripción de la imagen" fill />
        </div>
      </div>

      <div className="relative w-full flex items-center justify-center">
        <div className="flex flex-col items-center justify-center flex-grow p-5 relative z-10">
          <h1 className="text-4xl font-bold">Bienvenido a</h1>
          <h1 className="font-faster-stroker lg:text-4xl xl:text-5xl sm:text-2xl text-xl uppercase mt-2">Quantum Library</h1>

          <p className="mt-4 text-center">Una biblioteca virtual multiplataforma donde podrás almacenar, ordenar, categorizar y visualizar las estadísticas de tu contenido multimedia.</p>
          <p className="mt-4 text-center">La aplicación se centra en ofrecer funciones y características que harán que tu vena completacionista por fin tenga un lugar donde poder satisfacerse.</p>
          <p className="mt-4 text-center">Además, tendrás la posibilidad de crear bibliotecas compartidas con el contenido que tengas en común con tus amigos.</p>
        </div>
      </div>

      <div className=" flex md:flex-row flex-col max-w-[80%] md:max-h-[50%] h-auto py-5 gap-5 overflow-y-auto scrollbar-hide">
        <Card className="max-w-2xl md:overflow-hidden overflow-visible">
          <CardHeader className="text-xl text-gray-300">Contexto de Quantum Library</CardHeader>
          <CardBody className="text-gray-500 flex gap-4 md:overflow-y-auto overflow-visible">
            <p>
              Antiguamente se utilizaban estanterías y otros medios físicos para mostrar y almacenar todo el contenido que consumíamos y disfrutamos. Todo lo que hoy disfrutamos digitalmente alguna
              vez tuvo una existencia tangible: DVD para películas, libros, vinilos para música, cartuchos y discos para videojuegos.
            </p>
            <p>
              Hoy en día, el mundo está plagado de contenido multimedia online distribuido a lo largo de distintas plataformas y medios; series, películas, animes, videojuegos... Cada uno de estos
              tipos de contenido está fragmentado en diversas aplicaciones y servicios, lo que a menudo hace que el manejo y la organización de nuestras colecciones digitales sea un desafío.
            </p>
          </CardBody>
        </Card>
        <Card className="max-w-2xl md:overflow-hidden overflow-visible">
          <CardHeader className="text-xl text-gray-300">Características de Quantum Library</CardHeader>
          <CardBody className="text-gray-500 flex gap-4 md:overflow-y-auto overflow-visible">
            <p>
              Quantum Library se presenta como una solución innovadora que aborda la necesidad de organizar y acceder fácilmente a todo el contenido multimedia consumido. La implementación inicial se
              ha centrado en los videojuegos, generando una gran cantidad de datos relevantes para los usuarios.
            </p>
            <p>
              Dentro de nuestra biblioteca encontraremos diversas categorías prefabricadas para ordenar nuestros juegos. Los juegos se añadirán automáticamente al cumplirse las condiciones en las
              categorías de finalizados y completados, mientras que las categorías "backlog" permiten al usuario visualizar los juegos pendientes según la prioridad que desee asignar.
            </p>
            <p>
              Además, Quantum Library cuenta con un sistema de etiquetas que ayuda a encontrar el tipo de juego que más te apetezca jugar en un determinado momento. La función de selección aleatoria
              también está presente, mostrando un juego al azar dentro de los filtros aplicados por el usuario.
            </p>
          </CardBody>
        </Card>
        <Card className="max-w-2xl md:overflow-hidden overflow-visible">
          <CardHeader className="text-xl text-gray-300">Estadísticas y Grupos</CardHeader>
          <CardBody className="text-gray-500 flex gap-4 md:overflow-y-auto overflow-visible">
            <p>Para los completacionistas, se ha diseñado una sección de estadísticas que mostrará gráficos relevantes, como el porcentaje de juegos completados y la media de logros obtenidos.</p>
            <p>
              Quantum Library también permite crear grupos donde los amigos pueden votar por los títulos a jugar, facilitando la toma de decisiones en grupo. La integración con APIs, como la de Steam,
              permite importar tu biblioteca de juegos de forma sencilla, complementando así la experiencia del usuario.
            </p>
          </CardBody>
        </Card>
      </div>
    </section>
  );
}
