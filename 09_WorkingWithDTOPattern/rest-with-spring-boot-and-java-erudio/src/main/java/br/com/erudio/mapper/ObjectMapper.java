package br.com.erudio.mapper;

// Importação de classes necessárias
import java.util.ArrayList;
import java.util.List;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

// Classe que fornece métodos para mapeamento de objetos
public class ObjectMapper {

    // Criação de um objeto Mapper utilizando o DozerBeanMapperBuilder, que configura um mapeador padrão
    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    // Método genérico para mapear um objeto de um tipo O para um tipo D
    public static <O, D> D parseObject(O origin, Class<D> destination) {
        // Utiliza o mapper para mapear o objeto origin para o tipo de classe destination
        return mapper.map(origin, destination);
    }

    // Método genérico para mapear uma lista de objetos de um tipo O para uma lista de objetos do tipo D
    public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
        // Criação de uma nova lista para armazenar os objetos mapeados
        List<D> destinationObjects = new ArrayList<D>();
        // Itera sobre cada objeto na lista de origem
        for (Object o : origin) {
            // Adiciona o objeto mapeado à lista de destino
            destinationObjects.add(mapper.map(o, destination));
        }
        // Retorna a lista de objetos mapeados
        return destinationObjects;
    }
}
