package br.com.ifpe.oxefood.api.entregador;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.ifpe.oxefood.modelo.Entregador.Entregador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntregadorRequest {

   private String nome;

   @JsonFormat(pattern = "dd/MM/yyyy")
   private LocalDate dataNascimento;

   private String cpf;

   private String foneCelular;

   private String qtdEntregasRealizadas;
   private String valorPorFrete;
   private String rua;
   private String bairro;
   private String cidade;
   private String numero;
   private String cep;
   private String uf;
   private String complemento;
   private String ativo;
   private String foneFixo;


   public Entregador build() {

       return Entregador.builder()
           .nome(nome)
           .dataNascimento(dataNascimento)
           .cpf(cpf)
           .foneCelular(foneCelular)
           .foneFixo(foneFixo)
           .qtdEntregaRealizadas(qtdEntregasRealizadas)
           .valorPorFrete(valorPorFrete)
           .rua(rua)
           .bairro(bairro)
           .cidade(cidade)
           .numero(numero)
           .cep(cep)
           .uf(uf)
           .complemento(complemento)
           .build();
   }

}
